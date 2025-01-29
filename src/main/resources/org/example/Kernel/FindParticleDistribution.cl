
__kernel void FindParticleDistribution (
    __global    Particle*   particles,
    __global    int*        gridDistribution,
    const       GridStruct  gridStructure,
    const       int         num_of_particles)
{
    int gid = get_global_id(0);

    if (gid >= num_of_particles) return;

    Particle particle = particles[gid];
    int x = (int)(particle.x / gridStructure.size.x);
    int y = (int)(particle.y / gridStructure.size.y);

    if (x > gridStructure.num.x || y > gridStructure.num.y) return;

    int numberOfGrid = y * gridStructure.num.x + x;

    atomic_add(&gridDistribution[numberOfGrid], 1);
}

