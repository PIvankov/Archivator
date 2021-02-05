package archivator;

import archivator.exception.WrongZipFileException;
import java.io.IOException;

public class Archiver {

    public static void main(String[] args) throws IOException {
        Operation operation = null;

        do {
            try {
                operation = Archiver.askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
                e.printStackTrace();
            }
        } while (operation != Operation.EXIT);
    }

    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Выберите операцию:");
        ConsoleHelper.writeMessage(Operation.CREATE.ordinal() + " - упаковать файл");
        ConsoleHelper.writeMessage(Operation.ADD.ordinal() + " - добавить файл в архив");
        ConsoleHelper.writeMessage(Operation.REMOVE.ordinal() + " - удалить файл из архива");
        ConsoleHelper.writeMessage(Operation.EXTRACT.ordinal() + " - распаковать архив");
        ConsoleHelper.writeMessage(Operation.CONTENT.ordinal() + " - просмотреть содержимое файла");
        ConsoleHelper.writeMessage(Operation.EXIT.ordinal() + " - выход");

        int operationIndex = ConsoleHelper.readInt();

        for (Operation operation: Operation.values()) {
            if (operationIndex == operation.ordinal()) return operation;
        }
        return null;
    }
}
