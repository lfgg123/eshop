package com.eshop.config;

import com.eshop.controller.AppPurchaseController;
import com.jfinal.config.Routes;

public class PurchaseRoutes extends Routes {

    @Override
    public void config() {
        this.add("/app/purchase/getProductListByProClass", AppPurchaseController.class);
    }
}
