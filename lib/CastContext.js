import { NativeModules } from 'react-native';

const GoogleCast = NativeModules.RnGoogleCast;

const RnGoogleCast = {
  getCastDevice: async () => GoogleCast.getCastDevice(),
  getCastState: async () => GoogleCast.getCastState(),
  castMedia: async (data) => {
    // TODO: improve this validation
    if (typeof data !== 'object') {
      throw new Error(
        'Cannot cast media without data or type of data is invalid',
      );
    }
    return GoogleCast.castMedia(data);
  },
  endSession: async (stopCasting = false) => GoogleCast.endSession(stopCasting),
  play: GoogleCast.play,
  pause: GoogleCast.pause,
  stop: GoogleCast.stop,
  seek: (playPosition) => GoogleCast.seek(playPosition),
  launchExpandedControls: GoogleCast.launchExpandedControls,
  showIntroductoryOverlay: GoogleCast.showIntroductoryOverlay,
  setVolume: (volume) => GoogleCast.setVolume(volume),
  initChannel: (channel) => GoogleCast.initChannel(channel),
  sendMessage: (channel, message) => GoogleCast.sendMessage(message, channel),
  showCastPicker: () => GoogleCast.showCastPicker()
  ,
};

export default RnGoogleCast;
