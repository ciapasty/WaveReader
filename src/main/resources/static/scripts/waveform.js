canvasWidth = 999;
canvasHeight = 150;

let waveform = [];
let colorLight;
let colorDark;
var progress = 0;

function setup() {
    let canvas = createCanvas(canvasWidth, canvasHeight);
    canvas.parent('waveform');

    let r = random(150, 230);
    let g = random(150, 230);
    let b = random(150, 230);
    
    colorLight = color(r, g, b);
    colorDark = color(r - 40, g - 40, b - 40);

    // Debug - Generate random waveform
    for (x = 0; x < canvasWidth/3; x++) {
        waveform.push(random(canvasHeight));
    }
}

function draw() {
    background(52,52,52);

    rectMode(CENTER);
    noFill();

    let waveWidth = canvasWidth/3;

    for (x = 0; x < waveWidth; x++) {
        if ((x/waveWidth)*100 < progress) {
            // Draw played audio darker
            stroke(colorDark);
        } else {
            stroke(colorLight);
        }
        rect(x*3, canvasHeight/2, 1, waveform[x]);
    }

    // Draw progress line
    stroke(255);
    let lineX = progress/100 * canvasWidth;
    line(lineX, 0, lineX, canvasHeight);
    
    if (mouseX > 0 && mouseX < canvasWidth && 
        mouseY > 0 && mouseY < canvasHeight)
    {
        stroke(140);
        line(mouseX, 0, mouseX, canvasHeight);
    }
}

function seekAudio() {
    if (mouseX > 0 && mouseX < canvasWidth && 
        mouseY > 0 && mouseY < canvasHeight)
    {
        seek(mouseX/canvasWidth);
    }
}

function mouseDragged() {
    audio.pause();
    seekAudio();
}

function mouseClicked() {
    seekAudio();
}
