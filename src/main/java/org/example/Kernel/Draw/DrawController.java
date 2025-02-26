package org.example.Kernel.Draw;

import org.example.JavaFX.Window;
import org.example.Kernel.Kernel;
import org.example.Kernel.KernelManager;
import org.example.Structs.Canvas;

public class DrawController {

    KernelManager kernelManager;

    private Kernel drawBackground;
    private Kernel drawParticles;

    public DrawController () {
        kernelManager = KernelManager.getInstance();

        drawBackground = new DrawBackgroundKernel();
        kernelManager.addKernel("DrawBackgroundKernel", drawBackground);

        drawParticles = new DrawParticlesKernel();
        kernelManager.addKernel("DrawParticlesKernel", drawParticles);
    }

    public void draw() {
        drawBackground.run();
        drawParticles.run();

        Window.getInstance().pixels = Canvas.getInstance().getCanvas();
    }

}
