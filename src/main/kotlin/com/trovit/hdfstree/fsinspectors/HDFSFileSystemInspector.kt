/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trovit.hdfstree.fsinspectors

import com.trovit.hdfstree.TreeNode
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.io.File
import java.io.IOException

private const val HDFS_SITE = "hdfs-site.xml"
private const val MAPRED_SITE = "mapred-site.xml"
private const val CORE_SITE = "core-site.xml"

private const val HADOOP_CONF_DIR = "HADOOP_CONF_DIR"

private const val HADOOP_HOME = "HADOOP_HOME"

class HDFSFileSystemInspector(verbose : Boolean) : FileSystemInspector(verbose) {
    private val fileSystem: FileSystem = FileSystem.get(hadoopConfiguration)

    override fun getEntry(currentPath: String) =
        fileSystem.getFileStatus(Path(currentPath)).let { TreeNode(it.path.name, it.isDirectory, it.len) }

    override fun list(currentPath: String): List<TreeNode> {
        try {
            verbose(currentPath)
            return fileSystem.listStatus(Path(currentPath))
                .map { TreeNode(it.path.name, it.isDirectory, it.len) }
                .sortedBy { it.name }
        } catch (e: IOException) {
            println(e.message)
        }
        return emptyList()
    }

    override fun addSubdirToCurrent(path: String, subdir: String) = Path(path, subdir).toString()

    private val hadoopConfiguration: Configuration
        get() {
            val confPath = listOf(
                HADOOP_CONF_DIR to System.getenv(HADOOP_CONF_DIR),
                HADOOP_HOME to System.getenv(HADOOP_HOME) + "/conf",
                "/etc/hadoop/conf").firstOrNull {
                verbose("Checking $it")
                File("$it/$CORE_SITE").exists()
            } ?: throw Exception("Not found Hadoop configuration.")
            verbose("Hadoop config path: $confPath")
            return Configuration(false).apply {
                listOf(HDFS_SITE, MAPRED_SITE, CORE_SITE).forEach {
                    verbose("Adding config file: $it")
                    addResource(Path("$confPath/$it"))
                }
                set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")
            }
        }
}