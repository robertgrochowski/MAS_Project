package mas.controller;

/**
 * An callback interface which informs about tab navigation events.
 * Its implemented by main controller (TabsController) and called
 * by all other tabs
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public interface PageNavigationCallback {
    void onNextPage();
    void onPreviousPage();
    void onCancel();
    void onConfirmOrder();
}
