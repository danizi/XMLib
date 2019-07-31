package com.xm.lib.component.tip.dlg

import android.view.View
import com.xm.lib.component.tip.dlg.core.IDialog
import java.util.*

/**
 * 苹果风格对话框
 */
class IOSDlg : IDialog {

    private var control: ResourceBundle.Control? = null
    private var p: P? = null

    override fun setIcon(id: Int): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTitle(title: String): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMessage(msg: String): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPositiveButton(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNeutralButton(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNegativeButton(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setItems(items: Any, click: Any): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSingleChoiceItems(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMultiChoiceItems(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setView(view: View?): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun show(): IDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 控制器
     */
    class Control {

    }

    /**
     * 相关参数
     */
    class P {

    }
}