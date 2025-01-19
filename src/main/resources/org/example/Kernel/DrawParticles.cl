
#define PARTICLE_COLOR (uchar4)(0, 255, 255, 255)

__kernel void DrawParticles (
    __global    Particle*   particles,
    __global    uchar4*     outPut,
    const       Boundary    boundary,
    const       int         num_of_particles)
{
    int gid = get_global_id(0);

    if (gid >= num_of_particles) {
        return;
    }

    Particle particle = particles[gid];


    int x_min = floor(particle.x - particle.radius);
    int x_max = ceil(particle.x + particle.radius);

    int y_min = floor(particle.y - particle.radius);
    int y_max = ceil(particle.y + particle.radius);

    if (x_min < 0) x_min = 0;
    if (x_max >= boundary.width) x_max = boundary.width - 1;

    if (y_min < 0) y_min = 0;
    if (y_max >= boundary.height) y_max = boundary.height - 1;

    for (int y = y_min; y <= y_max; y++) {
        for (int x = x_min; x <= x_max; x++) {
            if (particle.radius >= length(x, y, particle.x, particle.y)) {
                outPut[y * (int) boundary.width + x] = PARTICLE_COLOR;
            }
        }
    }
}


