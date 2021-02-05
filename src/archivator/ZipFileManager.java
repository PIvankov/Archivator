package archivator;

import archivator.exception.PathIsNotFoundException;
import archivator.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {

    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        if (!Files.isDirectory(zipFile.getParent())) Files.createDirectory(zipFile.getParent());

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();
                for (Path file : fileNames) {
                    addNewZipEntry(zipOutputStream, source, file);
                }
            } else throw new PathIsNotFoundException();
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws IOException {
        try (InputStream inputStream = Files.newInputStream(filePath.resolve(fileName))) {
            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry);
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws IOException {
        int byteReadFile = 0;
        while ((byteReadFile = in.read()) > 0) out.write(byteReadFile);
    }

    public List<FileProperties> getFilesList() throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        List<FileProperties> filePropertiesList = new ArrayList<>();

        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                copyData(zipInputStream, byteArrayOutputStream);

                filePropertiesList.add(new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getMethod()));
            }
        }

        return filePropertiesList;
    }

    public void extractAll(Path outputFolder) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        if (!Files.isDirectory(outputFolder)) Files.createDirectories(outputFolder);

        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path absolutePath = outputFolder.resolve(zipEntry.getName());

                if (!Files.isDirectory(absolutePath.getParent())) Files.createDirectories(absolutePath.getParent());

                try(OutputStream OutputStream = Files.newOutputStream(absolutePath)) {
                    copyData(zipInputStream, OutputStream);
                }
            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        Path tempFileZip = Files.createTempFile(zipFile.getFileName().toString(), "");

        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile));
            ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFileZip))) {

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (pathList.contains(Paths.get(zipEntry.getName()))) {
                    ConsoleHelper.writeMessage("Файл " + zipEntry.getName() + " удален.");
                } else {
                    zipOutputStream.putNextEntry(zipEntry);
                    copyData(zipInputStream, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
            }
        }
        Files.move(tempFileZip, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        Path tempFileZip = Files.createTempFile(zipFile.getFileName().toString(), "");

        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile));
            ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFileZip))) {

            ZipEntry zipEntry;
            List<Path> tempFileNamesList = new ArrayList<>();

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                if (tempFileNamesList.contains(zipEntry.getName())) {
                    ConsoleHelper.writeMessage("");
                    continue;
                }
                tempFileNamesList.add(Paths.get(zipEntry.getName()));

                zipOutputStream.putNextEntry(zipEntry);
                copyData(zipInputStream, zipOutputStream);
                zipOutputStream.closeEntry();
            }

            for (Path path : absolutePathList) {
                if (Files.exists(path) && Files.isRegularFile(path)) {
                    if (tempFileNamesList.contains(path.getFileName())) {
                        ConsoleHelper.writeMessage("Файл " + path + " уже есть в архиве.");
                    } else {
                        addNewZipEntry(zipOutputStream, path.getParent(), path.getFileName());
                        ConsoleHelper.writeMessage("Файл " + path + " добавлен в архив.");
                    }
                } else throw new PathIsNotFoundException();
            }

        }
        Files.move(tempFileZip, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }
}
