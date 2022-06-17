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
package com.trovit.hdfstree

private const val NOT_VALID = -1L

class TreeNode(val name: String, val isDirectory: Boolean, length: Long) {
    val children = mutableListOf<TreeNode>()
    val folders get() = children.filter { it.isDirectory }
    var size = if (isDirectory) NOT_VALID else length
        get() {
            if (field == NOT_VALID) {
                field = children.sumOf { it.size }
            }
            return field
        }
}