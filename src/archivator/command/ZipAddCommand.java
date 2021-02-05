package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;
import archivator.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Добавление файла в архив.");
            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Введите имя файлов, которые хотите добавить в архив");
            Path filePathForArchiver = Paths.get(ConsoleHelper.readString());

            zipFileManager.addFile(filePathForArchiver);
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
