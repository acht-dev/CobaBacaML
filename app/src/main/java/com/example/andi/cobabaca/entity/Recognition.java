package com.example.andi.cobabaca.entity;

import android.graphics.Bitmap;

import java.util.List;

public interface Recognition {

    List<Classifier> recognize(Bitmap bitmap);

    void close();
}
