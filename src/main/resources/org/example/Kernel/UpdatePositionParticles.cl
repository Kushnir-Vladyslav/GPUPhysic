
#define FREE_FALL   9.8

__kernel void UpdatePositionParticles (
    __global    Particle*   particles,
    const       int         num_of_particles
    const       int         time)
{
    int gid = get_global_id(0);

    if (gid >= num_of_particles) {
        return;
    }

    Particle particle = particles[gid_x];

    particle.xSpeed += FREE_FALL * time;

    particle.x += xSpeed * time;
    particle.y += ySpeed * time;

    particles[gid_x] = particle;
}

