
package com.example.insta.interfacee;
public interface EditImageFragmentListener {
    void onBrightnessChanged(int brightness);
    void onSaturationChanged(float saturation) ;
    void onConstrantChanged(float constrant);
    void onEditStarted();
    void onEditCompleted();
}
