/**
 *
 */
package io.github.rosestack.constants;


import java.io.File;

import static io.github.rosestack.constants.PathConstants.SLASH;
import static io.github.rosestack.constants.SymbolConstants.EXCLAMATION;
import static java.io.File.pathSeparator;
import static java.io.File.separator;
import static java.lang.System.lineSeparator;

/**
 * Separator Constants
 * <p/>
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public interface SeparatorConstants {

    /**
     * Archive Entry Separator : "!/"
     */
    String ARCHIVE_ENTRY_SEPARATOR = EXCLAMATION + SLASH;

    /**
     * File Separator : {@link File#separator}
     */
    String FILE_SEPARATOR = separator;

    /**
     * Path Separator : {@link File#pathSeparator}
     */
    String PATH_SEPARATOR = pathSeparator;

    /**
     * Line Separator : {@link System#lineSeparator()}
     */
    String LINE_SEPARATOR = lineSeparator();

}
