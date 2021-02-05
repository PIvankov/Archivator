package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;
import archivator.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Распаковка архива.");
            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Введите имя директории, в которую будет распакован архив");
            Path outputDirectory = Paths.get(ConsoleHelper.readString());

            zipFileManager.extractAll(outputDirectory);
            ConsoleHelper.writeMessage("Распаковка архива завершена");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя директории.");
        }
    }
}
