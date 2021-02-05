package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipRemoveCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Введите имя файла, который нужно удалить");
        Path fileForDelete = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(fileForDelete);
    }
}
