package com.oyy.strong.view.drawer;

/**
 * Interface used to communicate from the v21-specific code for configuring a DrawerLayout
 * to the DrawerLayout itself.
 */
interface DrawerLayoutImpl {
    void setChildInsets(Object insets, boolean drawStatusBar);
}
