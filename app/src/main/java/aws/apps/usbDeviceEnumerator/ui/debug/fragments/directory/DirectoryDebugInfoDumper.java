package aws.apps.usbDeviceEnumerator.ui.debug.fragments.directory;

import android.content.Context;
import android.text.SpannableStringBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import androidx.annotation.NonNull;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DebugInfoDumper;

public class DirectoryDebugInfoDumper implements DebugInfoDumper {
    private static final Comparator<File> FILE_COMPARATOR = new FileComparator();
    private static final String FILE_PREFIX = "    ";
    private static final String DIR_PREFIX = "[D] ";
    private static final char BOX_CORNER = '\u2514';

    private final Context context;
    private final String dir;

    public DirectoryDebugInfoDumper(final Context context, final String dir) {
        this.context = context;
        this.dir = dir;
    }

    @NonNull
    @Override
    public CharSequence dump() {
        return getDump(context, new File(dir));
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public static CharSequence getDump(@NonNull final Context context,
                                       @NonNull final File dir) {
        final SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Directory '" + dir + "':\n\n");

        final boolean possibleError;
        if (!dir.exists()) {
            possibleError = true;
            sb.append("Directory does not exist!");
        } else if (!dir.isDirectory()) {
            possibleError = true;
            sb.append("Not a directory!");
        } else {
            final File[] children = dir.listFiles();
            if (children == null) {
                possibleError = true;
                sb.append("Directory has null children! - Access permission issues?");
            } else {
                if (children.length == 0) {
                    possibleError = true;
                    sb.append("Directory has " + children.length + " children.");
                } else {
                    possibleError = false;
                    sb.append("Directory has " + children.length + " children:");
                    sb.append("\n");
                    addChildren(sb, dir, 0, 1);
                }
            }
        }

        if (possibleError) {
            sb.append("\n\n");
            sb.append(context.getString(R.string.debug_unexpected_result_explanation));
        }

        return sb.toString();
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    private static void addChildren(final SpannableStringBuilder sb,
                                    final File parent,
                                    final int depth,
                                    final int maxDepth) {
        final File[] children = parent.listFiles();
        if (children != null && children.length > 0) {
            final String dirPrefix = getPrefix(depth, DIR_PREFIX);
            final String filePrefix = getPrefix(depth, FILE_PREFIX);

            Arrays.sort(children, FILE_COMPARATOR);
            for (final File child : children) {
                sb.append("\n");
                final String name = depth == 0 ? child.getAbsolutePath() : child.getName();
                if (child.isDirectory()) {
                    sb.append(dirPrefix + name);
                    if (depth < maxDepth) {
                        addChildren(sb, child, depth + 1, maxDepth);
                    }
                } else {
                    sb.append(filePrefix + name);
                }
            }
        }
    }

    private static String getPrefix(final int depth, final String suffix) {
        final boolean root = depth == 0;
        final String spaces = root ? "" : new String(new char[DIR_PREFIX.length()]).replace('\0', ' ');
        final String boxCorner = root ? "" : Character.toString(BOX_CORNER);

        return spaces + boxCorner + suffix;
    }

    private static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs == null && rhs == null) {
                return 0;
            } else if (lhs == null || rhs == null) {
                return lhs == null ? -1 : 1;
            }
            int cmp = compare(lhs.getParentFile(), rhs.getParentFile());
            if (cmp == 0) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                cmp = lhs.getName().compareTo(rhs.getName());
            }
            return cmp;
        }
    }
}
