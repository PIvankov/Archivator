package archivator.command;

import archivator.ConsoleHelper;
import archivator.ZipFileManager;

public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Просмотр содержимого архива.");

        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Содержимое архива:");

        zipFileManager.getFilesList().stream()
                .forEach(System.out::println);

        ConsoleHelper.writeMessage("Содержимое архива прочитано.");
    }
}
