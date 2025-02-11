
#define PARTICLE_COLOR (uchar4)(0, 255, 255, 255)

uchar4 colors[] = {
    (uchar4)(0, 255, 255, 255),   // Cyan
    (uchar4)(255, 255, 0, 255),   // Yellow
    (uchar4)(255, 0, 255, 255),   // Magenta
    (uchar4)(0, 0, 255, 255),     // Blue
    (uchar4)(0, 255, 0, 255),     // Green
    (uchar4)(255, 0, 0, 255),     // Red
    (uchar4)(128, 128, 128, 255), // Gray
    (uchar4)(255, 255, 255, 255), // White
    (uchar4)(0, 0, 0, 255),       // Black
    (uchar4)(255, 128, 0, 255),   // Orange
    (uchar4)(128, 0, 255, 255),   // Violet
    (uchar4)(0, 128, 255, 255),   // Light Blue
    (uchar4)(128, 255, 0, 255),   // Lime Green
    (uchar4)(255, 0, 128, 255),   // Pink
    (uchar4)(0, 255, 128, 255),   // Mint Green
    (uchar4)(128, 0, 128, 255),   // Purple
    (uchar4)(128, 128, 0, 255),   // Olive
    (uchar4)(0, 128, 128, 255),   // Teal
    (uchar4)(192, 192, 192, 255), // Silver
    (uchar4)(255, 215, 0, 255),   // Gold
    (uchar4)(75, 0, 130, 255),    // Indigo
    (uchar4)(139, 69, 19, 255),   // Brown
    (uchar4)(244, 164, 96, 255),  // Sandy Brown
    (uchar4)(255, 20, 147, 255),  // Deep Pink
};

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

    uchar4 collor = colors[gid % (sizeof(colors) / sizeof(colors[0]))];

    for (int y = y_min; y <= y_max; y++) {
        for (int x = x_min; x <= x_max; x++) {
            if (particle.radius >= length((float2)(x - particle.x, y - particle.y))) {
                outPut[y * (int) boundary.width + x] = collor;
            }
        }
    }
}


