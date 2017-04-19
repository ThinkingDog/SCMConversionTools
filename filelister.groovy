def debug = false  // Should be parameterized for Jenkins

def folder = ".\\BigCountry" // Should be parameterized in Jenkins
def fileCount = 0
def dirCount = 0
def fileSizeTotal = 0f
def maxFileSize = 0f
def newFile = "./filelist.out"  // Should be parameterized in Jenkins
def dirPattern = "\\.git"  // Should be parameterized in Jenkins
def dirIgnorePattern = ~/${dirPattern}/

PrintWriter printWriter = new PrintWriter(newFile)

new File(folder).eachDirRecurse() { dir ->

    if (!dirPattern.isEmpty() && dirIgnorePattern.matcher(dir.name).find()) { if (debug) { println "Skipping " + dir.name }; return }  // Skip any directory matching ignore pattern

    dir.eachFileMatch(~/.*/) { file -> 

        if (!dirPattern.isEmpty() && dirIgnorePattern.matcher(file.getPath()).find()) { if (debug) {println "Skipping " + dir.name}; ; return }  // Skip any file path that has ignore pattern

        if (file.isDirectory()) {
            println file.getPath() + " is a directory"
            printWriter.println(file.getPath() + " is a directory ")
            dirCount++
        } else {
            fileSizeTotal = fileSizeTotal + file.length()
            if (maxFileSize < file.length()) { maxFileSize = file.length() }
            println file.getPath() + " " + file.length() + " bytes"
            printWriter.println(file.getPath() + " " + file.length() + " bytes")
            fileCount++
        }
    }
}

// Convert total file size bytes into Mb and Gb
def fileSizeTotalM = fileSizeTotal / 1024f / 1024f
def fileSizeTotalG = fileSizeTotalM / 1024f
def maxFileSizeM = maxFileSize / 1024f / 1024f
def avgFileSizeM = fileSizeTotal / fileCount / 1024f / 1024f
println " "
printWriter.println(" ")
println "Files: " + fileCount + " Dirs: " + dirCount + " Size: " + fileSizeTotalM.round(2) + " Mb " + fileSizeTotalG.round(2) + " Gb - Max file size: " + maxFileSizeM.round(2) + " Mb - Avg file size: " + avgFileSizeM.round(2) + " Mb"
printWriter.println("Files: " + fileCount + " Dirs: " + dirCount + " Size: " + fileSizeTotalM.round(2) + " Mb " + fileSizeTotalG.round(2) + " Gb - Max file size: " + maxFileSizeM.round(2) + " Mb - Avg file size: " + avgFileSizeM.round(2) + " Mb")

printWriter.close()