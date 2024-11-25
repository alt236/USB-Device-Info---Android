/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package aws.apps.usbDeviceEnumerator.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory
import aws.apps.usbDeviceEnumerator.ui.common.Navigation
import aws.apps.usbDeviceEnumerator.ui.dbupdate.DatabaseUpdater
import aws.apps.usbDeviceEnumerator.ui.debug.DebugActivity
import aws.apps.usbDeviceEnumerator.ui.main.list.UiUsbDevice
import aws.apps.usbDeviceEnumerator.ui.main.list.UsbDeviceListAdapter
import aws.apps.usbDeviceEnumerator.ui.main.list.UsbDeviceListDataMapper
import aws.apps.usbDeviceEnumerator.ui.main.tabs.TabController
import aws.apps.usbDeviceEnumerator.ui.main.tabs.TabViewHolder
import aws.apps.usbDeviceEnumerator.ui.progress.ProgressDialogControl
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.FragmentFactory
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper.ApiConditionalResultMapper
import dagger.hilt.android.AndroidEntryPoint
import uk.co.alt236.androidusbmanager.AndroidUsbManager
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbManager
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyInfo
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyLogo
import uk.co.alt236.usbinfo.database.providers.DataProviderUsbInfo
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG: String = javaClass.name

    @Inject
    lateinit var usbManagerLinux: SysBusUsbManager

    @Inject
    lateinit var usbManagerAndroid: AndroidUsbManager

    @Inject
    lateinit var dbUsb: DataProviderUsbInfo

    @Inject
    lateinit var dbComp: DataProviderCompanyInfo

    @Inject
    lateinit var zipComp: DataProviderCompanyLogo

    @Inject
    lateinit var usbListDataMapper: UsbDeviceListDataMapper

    @Inject
    lateinit var apiConditionalResultMapper: ApiConditionalResultMapper

    @Inject
    lateinit var infoFragmentFactory: FragmentFactory

    private var navigation: Navigation? = null

    private var tabController: TabController? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        tabController = createTabController()
        navigation = Navigation(this, infoFragmentFactory)
        checkIfDbPresent()
        refreshUsbDevices()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.menu_about -> {
                AboutDialogFactory.createAboutDialog(this).show()
                return true
            }

            R.id.menu_debug -> {
                val intent = Intent(this, DebugActivity::class.java)
                ActivityCompat.startActivity(this, intent, null)
                return true
            }

            R.id.menu_update_db -> {
                val control = ProgressDialogControl(supportFragmentManager)
                val databaseUpdater = DatabaseUpdater(control, dbComp, dbUsb, zipComp)

                databaseUpdater.start(this)
                return true
            }

            R.id.menu_refresh -> {
                refreshUsbDevices()
                return true
            }

            else -> return false
        }
    }

    private fun createTabController(): TabController {
        val tabController = TabController(this)

        tabController.setup { tabId: String, tabViewHolder: TabViewHolder ->
            this.onTabChanged(tabId, tabViewHolder)
        }

        // Setup android list - tab1;
        tabController.getHolderForTag(TabController.TAB_ANDROID_INFO)
            .list.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>, _: View?, position: Int, _: Long ->
                onListItemClicked((parent as ListView), position)
            }

        // Setup linux list - tab2
        tabController.getHolderForTag(TabController.TAB_LINUX_INFO)
            .list.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>, _: View?, position: Int, _: Long ->
                onListItemClicked((parent as ListView), position)
            }

        return tabController
    }

    private fun onListItemClicked(listView: ListView, position: Int) {
        listView.setItemChecked(position, true)
        navigation?.showUsbDeviceInfo(listView.getDeviceAtPosition(position))
    }

    private fun onTabChanged(
        @Suppress("UNUSED_PARAMETER") tabId: String,
        tabViewHolder: TabViewHolder
    ) {
        if (navigation!!.isSmallScreen) {
            return
        }

        val listView = tabViewHolder.list
        val checkedItemPosition = listView.checkedItemPosition

        val fragment = if (checkedItemPosition == ListView.INVALID_POSITION) {
            null
        } else {
            val device = listView.getDeviceAtPosition(checkedItemPosition)
            infoFragmentFactory.getFragment(device)
        }

        if (fragment == null) {
            navigation!!.removeFragmentsFromContainer()
        } else {
            navigation!!.stackFragment(fragment)
        }
    }


    private fun refreshUsbDevices() {
        updateList(
            tabController!!.getHolderForTag(TabController.TAB_ANDROID_INFO),
            usbManagerAndroid.getDeviceList()
        )

        updateList(
            tabController!!.getHolderForTag(TabController.TAB_LINUX_INFO),
            usbManagerLinux.usbDevices
        )
    }

    private fun updateList(holder: TabViewHolder, map: Map<String, *>) {
        val devices = usbListDataMapper.map(map)

        val adapter: ListAdapter = UsbDeviceListAdapter(
            applicationContext,
            devices,
            apiConditionalResultMapper
        )

        holder.list.adapter = adapter

        val count = getString(R.string.text_number_of_devices, devices.size)
        holder.count.text = count
    }

    private fun checkIfDbPresent() {
        // Prompt user to DL db if it is missing.
        if (!File(dbUsb.dataFilePath).exists()) {
            DialogFactory.createOkDialog(
                this,
                R.string.alert_db_not_found_title,
                R.string.alert_db_not_found_instructions
            ).show()
            Log.w(TAG, "^ Database not found: " + dbUsb.dataFilePath)
        }
    }

    private fun ListView.getDeviceAtPosition(position: Int) =
        this.getItemAtPosition(position) as UiUsbDevice
}
