package com.github.metallnt.cleanup.actions;

import com.github.metallnt.cleanup.CleanUp;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class com.github.metallnt.cleanup.actions
 * <p>
 * Date: 24.12.2021 19:45 24 12 2021
 *
 * @author Metall
 */
public class DeleteDirs {

    private final CleanUp plugin;
    private String homeDir = "plugins";
    private List<String> dirsPlugins;
    private long period;
    private long nowTime;
    private Date lastModified;
    private List<String> plugins;
    private List<String> dirsProtections;

    public DeleteDirs(CleanUp cleanUp) {
        this.plugin = cleanUp;
        onDeleteDirs();
    }

    public void onDeleteDirs() {
        if (plugin.getDefaultConfig().getDeleteDirs()) {
            dirsPlugins = findFoldersInDirectory(homeDir);
            period = (long) plugin.getDefaultConfig().getPeriodDeleteDirs() * 1000 * 60 * 60 * 24; // sec + min + hour + day
            nowTime = new Date().getTime();
            CleanUp.log.info("Обнаружены папки: " + dirsPlugins.toString());
            for (String dir : dirsPlugins) {
                // Если дата изменения + период меньше сегодня
                if (getLastModified(new File(homeDir + dir)) + period < nowTime) {
                    if (!checkPlugin(dir)) {
                        CleanUp.log.info("Для папки " + dir + " плагин ненайден");
                        if (!checkProtectionFolder(dir)) {
                            if (deleteDirectory(new File(dir))) {
                                CleanUp.log.info("Директория " + dir + " успешно удалена");
                            } else {
                                CleanUp.log.info("Директория " + dir + " НЕ удалена");
                            }
                        }
                    } else {
                        CleanUp.log.info("Директория " + dir + " используется плагином");
                    }

                }
            }
        }
    }

    private boolean checkProtectionFolder(String dir) {
        dirsProtections = Arrays.asList(plugin.getDefaultConfig().getProtectionFolders().getItems());
        for (String prot : dirsProtections) {
            if (prot.equals(dir)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPlugin(String dir) {
        plugins = getPlugins();
        CleanUp.log.info("Обнаружены плагины: " + plugins);
        for (String p : plugins) {
            if (p.equalsIgnoreCase(dir)) {
                return true;
            }
        }
        return false;
    }

    // Получаем дату последнего изменения папки или файлов внутри
    private long getLastModified(File directory) {
        CleanUp.log.info(directory.toString());
//        File[] files = directory.listFiles();
//        CleanUp.log.info(Arrays.toString(files));
//        assert files != null;
//        if (directory.length == 0) {
            return new Date(directory.lastModified()).getTime();
//        }
//        Arrays.sort(files, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
//        return new Date(files[0].lastModified()).getTime();
    }

    // Список папок в plugins
    private List<String> findFoldersInDirectory(String path) {
        File directory = new File(path);
        FileFilter directoryFileFilter = File::isDirectory;
        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        assert directoryListAsFile != null;
        List<String> foldersInDirectory = new ArrayList<>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }
        return foldersInDirectory;
    }

    private List<String> findFilesInDirectory(String path) {
        File directory = new File(path);
        FileFilter directoryFileFilter = File::isFile;
        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        assert directoryListAsFile != null;
        List<String> filesInDirectory = new ArrayList<>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            filesInDirectory.add(directoryAsFile.getName());
        }
        return filesInDirectory;
    }

    private List<String> getPlugins() {
        return Collections.singletonList(Arrays.toString(plugin.getServer().getPluginManager().getPlugins()));
    }

    private boolean deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        CleanUp.log.info("Удалено: " + directory);
        return directory.delete();
    }
}
