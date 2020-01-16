import { NativeModules } from "react-native";

const { RnGoogleCast: GoogleCast } = NativeModules;

const RnGoogleCast = {
  getCastDevice: async () => {
    return GoogleCast.getCastDevice();
  },
  getCastState: async () => {
    return await GoogleCast.getCastState();
  },
  castMedia: async data => {
    if (typeof data !== "object") {
      throw new Error(
        "Cannot cast media without data or type of data is invalid"
      );
    }
    return GoogleCast.castMedia(data);
  },
  endSession: async (stopCasting = false) => {
    return await GoogleCast.endSession(stopCasting);
  },
  play: GoogleCast.play,
  pause: GoogleCast.pause,
  stop: GoogleCast.stop,
  seek(playPosition) {
    return GoogleCast.seek(playPosition);
  },
  launchExpandedControls: GoogleCast.launchExpandedControls,
  showIntroductoryOverlay: GoogleCast.showIntroductoryOverlay,
  setVolume(volume) {
    return GoogleCast.setVolume(volume);
  },
  initChannel(namespace) {
    return GoogleCast.initChannel(namespace);
  },
  sendMessage(namespace, message) {
    return GoogleCast.sendMessage(message, namespace);
  },
  showCastPicker() {
    GoogleCast.showCastPicker();
  }
};

export default RnGoogleCast;
