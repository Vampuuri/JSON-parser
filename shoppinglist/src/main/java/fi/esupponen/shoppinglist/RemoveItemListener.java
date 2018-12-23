package fi.esupponen.shoppinglist;

import javafx.event.*;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1217
 * @since       2018-1217
 */
public class RemoveItemListener implements EventHandler<ActionEvent> {

    /**
     * Gui that hosts the button.
     */
    Gui host;

    /**
     * Index of the element that should be removed.
     */
    int index;

    /**
     * Basic constructor.
     *
     * @param   host    host of the button
     * @param   index   index of the element to be removed  
     */
    public RemoveItemListener(Gui host, int index) {
        this.host = host;
        this.index = index;
    }

    /**
     * Removes given index from host's list and updated itemTable.
     *
     * @param   e   not used
     */
    @Override
    public void handle(ActionEvent e) {
        host.list.getUnits().remove(index);
        host.updateItemTable();
    }
}