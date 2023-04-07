$(document).ready(function() {
  $('#video-link').on('input', function() {
    var videoLink = $(this).val();
    var videoId = getVideoId(videoLink);
    if (videoId !== '') {
      var videoPlayer = document.getElementById('video-player');
      videoPlayer.src = 'https://www.youtube.com/embed/' + videoId + '?autoplay=1';
    }
  });

  $('#video-form').submit(function(e) {
    e.preventDefault();

    // Show the loading bar
    $('#loading-bar').removeClass('hidden');
    // Simulate progress
    var progress = 0;
    var interval = setInterval(function() {
      progress += Math.random() * 10;
      if (progress >= 100) {
        progress = 100;
        clearInterval(interval);
      }
      $('#progress').width(progress + '%');
    }, 200);

    var videoLink = $('#video-link').val();
    var startTime = $('#start-time').val();
    var endTime = $('#end-time').val();

    var videoId = getVideoId(videoLink);

    var videoPlayer = document.getElementById('video-player');
    videoPlayer.src = 'https://www.youtube.com/embed/' + videoId + '?autoplay=1';

    var downloadUrl = '/api/youtube/v1/downloads?videoLink=' + videoLink;

    if (startTime !== '') {
      downloadUrl += '&startTime=' + startTime;
    }

    if (endTime !== '') {
      downloadUrl += '&endTime=' + endTime;
    }

    // Fetch the video and start the download
    fetch(downloadUrl)
      .then((response) => {
        if (response.ok) {
          clearInterval(interval);
          $('#progress').width('100%');
          return response.blob();
        } else {
          throw new Error('Network response was not ok');
        }
      })
      .then((blob) => {
        $('#loading-bar').addClass('hidden');
        $('#progress').width('0');
        var url = URL.createObjectURL(blob);
        var downloadButton = document.createElement('a');
        downloadButton.href = url;
        downloadButton.download = 'video.mp4';
        downloadButton.style.display = 'none';
        document.body.appendChild(downloadButton);
        downloadButton.click();
        document.body.removeChild(downloadButton);
      })
      .catch((error) => {
        console.error('Error fetching video:', error);
        $('#loading-bar').addClass('hidden');
        $('#progress').width('0');
        clearInterval(interval);
      });

    $('#video-form')[0].reset();
  });

  function getVideoId(videoLink) {
    var regex = /[?&]v=([^&#]*)|youtu\.be\/([^&#]*)/;
    var match = videoLink.match(regex);
    if (match !== null) {
      return match[1] || match[2];
    } else {
      return '';
    }
  }
});
