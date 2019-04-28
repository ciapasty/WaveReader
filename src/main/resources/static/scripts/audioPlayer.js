var audio = new Audio('sample.mp3');

audio.ontimeupdate = function() {
    progress = 100*audio.currentTime/audio.duration;
}

function playAudio() {
    if (audio.paused) {
        audio.play();
        playButton.className = "pause";
    } else {
        audio.pause();
        playButton.className = "play";
    }
}

function stopAudio() {
    audio.pause();
    audio.currentTime = 0.0;
    playButton.className = "play";
}

function setVolume(volume) {
    audio.volume = volume;
}

function seek(progress) {
    audio.currentTime = audio.duration * progress;
}