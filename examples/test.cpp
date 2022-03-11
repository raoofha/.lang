
#include <canvas.h>
#include <stdint.h>

const int w = 1920;
const int h = 1000;
Canvas c{w, h};
ImageData image{w, h};

int main() {
    for (int y = 0; y < h; ++y) {
        for (int x = 0; x < w; ++x) {
            image.data[y * w + x] = RGB(x | y, 0, 0);
        }
    }
    image.commit();
    c.putImageData(image, 0, 0);

    const char* msg = "hello from c++";
    c.setFillStyle("white");
    c.setFont("bold 100px sans");
    c.fillText(msg, (w - c.measureText(msg)) / 2, (h + 100) / 2);
}
