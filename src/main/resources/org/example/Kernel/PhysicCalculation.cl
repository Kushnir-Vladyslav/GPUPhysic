
__kernel void PhysicCalculation(
    __global    Particle*   particles,
    const       int         num_of_particles)
{
    int gid_x = get_global_id(0);
    int gid_y = get_global_id(1);

    if (gid_x >= num_of_particles ||
        gid_y >= num_of_particles ||
        gid_y <= gid_x) {
        return;
    }

    Particle mainParticle = particles[gid_x];
    Particle secondParticle = particles[gid_y];

    mainParticle.yAcceleration += FREE_FALL;
}

