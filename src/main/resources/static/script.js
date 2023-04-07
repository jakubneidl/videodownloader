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

    // Show the spinning loader
    $('#loading-bar').css('display', 'flex');

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
          return response.blob();
        } else {
          throw new Error('Network response was not ok');
        }
      })
      .then((blob) => {
        $('#loading-bar').css('display', 'none');
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
        $('#loading-bar').css('display', 'none');
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
