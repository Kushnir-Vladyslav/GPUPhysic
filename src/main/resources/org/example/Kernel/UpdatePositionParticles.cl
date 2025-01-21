
#define FREE_FALL 90.8

__kernel void UpdatePositionParticles (
    __global    Particle*   particles,
    const       int         num_of_particles,
    const       float         time)
{
    int gid = get_global_id(0);
    if (gid >= num_of_particles) return;

    Particle particle = particles[gid];

    particle.ySpeed += FREE_FALL * time;

    // Застосовуємо демпфінг
 //   particle.xSpeed *= DAMPING;
 //   particle.ySpeed *= DAMPING;

    particle.x += particle.xSpeed * time;
    particle.y += particle.ySpeed * time;

    particles[gid] = particle;
}

