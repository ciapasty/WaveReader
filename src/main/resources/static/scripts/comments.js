debugComments = [
    {
        "user": "User1",
        "time": 120,
        "text": "Really nice beat!"
    },
    {
        "user": "User2",
        "time": 25,
        "text": "Comments are lame"
    },
    {
        "user": "User3",
        "time": 180,
        "text": "This is the end"
    },
    {
        "user": "User4",
        "time": 60,
        "text": "Asdf"
    },
]

function createCommentTable() {
    
}

function addComment() {
    let parent = document.getElementById('comments')

    let image = new Image(50, 50);
    parent.appendChild(image);

    image.src = 'avatar.png';
    image.style.position = 'absolute';
    let position = canvasWidth*audio.currentTime/audio.duration;
    image.style.left = (position - 25) + 'px';

    let textInput = document.createElement('input');
    textInput.setAttribute('type', 'text');
    parent.appendChild(textInput)

    textInput.src = 'avatar.png';
    textInput.style.position = 'absolute';
    textInput.style.left = (position + 25) + 'px';

    textInput.addEventListener('keyup', returnKeyListener);
}

function returnKeyListener(event) {
    if (event.keyCode(13)) {
        if (textInput.value == "") {
            
        }
    }
}