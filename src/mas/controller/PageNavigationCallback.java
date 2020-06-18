package mas.controller;

public interface PageNavigationCallback {
    void onNextPage();
    void onPreviousPage();
    void onCancel();
    void onConfirmOrder();
}
