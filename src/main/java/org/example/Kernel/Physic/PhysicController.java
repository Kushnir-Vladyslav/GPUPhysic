package org.example.Kernel.Physic;

import org.example.Kernel.Kernel;
import org.example.Kernel.KernelManager;

public class PhysicController {
    KernelManager kernelManager;

    Kernel boundaryCollision;
    Kernel physicCalculation;
    Kernel updatePositionParticles;

    public PhysicController() {
        kernelManager = KernelManager.getInstance();

        boundaryCollision = new BoundaryCollisionKernel();
        kernelManager.addKernel("BoundaryCollision", boundaryCollision);

        physicCalculation = new PhysicCalculationKernel();
        kernelManager.addKernel("PhysicCalculation", physicCalculation);

        updatePositionParticles = new UpdatePositionParticlesKernel();
        kernelManager.addKernel("UpdatePositionParticles", updatePositionParticles);
    }

    public void runPhysic () {
        for (int i = 0; i < 10; i++) {
            physicCalculation.run();
            boundaryCollision.run();
        }
        updatePositionParticles.run();
    }
}
