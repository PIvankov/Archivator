package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ZipCommand implements Command {
    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Введите полный путь файла архива");
        String zipFile = ConsoleHelper.readString();
        Path zipFilePath = Paths.get(zipFile);
        return new ZipFileManager(zipFilePath);
    }
}
