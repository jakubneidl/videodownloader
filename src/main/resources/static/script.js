
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

    var downloadButton = document.createElement('a');
    downloadButton.href = downloadUrl;
    downloadButton.innerText = 'Download Video';
    downloadButton.download = 'video.mp4';
    document.body.appendChild(downloadButton);

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