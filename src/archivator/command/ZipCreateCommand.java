package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;
import archivator.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Создание архива.");
            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Введите имя файла/директории, которые хотите заархивировать");
            Path filePathForArchiver = Paths.get(ConsoleHelper.readString());

            zipFileManager.createZip(filePathForArchiver);
            ConsoleHelper.writeMessage("Архив создан.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
