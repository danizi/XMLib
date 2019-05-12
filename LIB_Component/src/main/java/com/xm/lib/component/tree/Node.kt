package com.xm.lib.component.tree

import java.util.*

/**
 * 节点
 */
class Node private constructor(builder: Builder) {
    var title: String = ""
    var isExpand: Boolean? = false
    var parent: Node? = null
    var childs = ArrayList<Node>()

    init {

        this.title = builder.title
        this.isExpand = builder.isExpand
        this.parent = builder.parent
        this.childs = builder.childs!!
    }

    class Builder {
        var title: String = ""
        var isExpand: Boolean? = false
        var parent: Node? = null
        var childs:ArrayList<Node>?=null

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun isExpand(isExpand: Boolean): Builder {
            this.isExpand = isExpand
            return this
        }

        fun parent(parent: Node): Builder {
            this.parent = parent
            return this
        }

        fun childs(childs: ArrayList<Node> ? =ArrayList<Node>()): Builder {
            this.childs = childs
            return this
        }

        fun build(): Node {
            val node = Node(this)
            for (childNode in childs?.iterator()!!){
                childNode.parent = node
            }
            return node
        }
    }
}