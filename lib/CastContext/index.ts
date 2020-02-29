import { NativeModules } from 'react-native';

import {
  CastMediaProps,
  CastStateProps,
  CastDeviceProps,
} from '../types/CastContext';
const GoogleCast = NativeModules.RnGoogleCast;

async function getCastDevice(): Promise<CastDeviceProps | null> {
  return GoogleCast.getCastDevice();
}

async function getCastState(): Promise<CastStateProps> {
  return GoogleCast.getCastState();
}

async function castMedia(media: CastMediaProps): Promise<CastMediaProps> {
  // TODO: improve this validation
  if (typeof media !== 'object') {
    throw new Error(
      'Cannot cast media without data or type of data is invalid',
    );
  }
  return GoogleCast.castMedia(media);
}

async function endSession(stopCasting: boolean = false) {
  return GoogleCast.endSession(stopCasting);
}

const CastContext = {
  getCastDevice,
  getCastState,
  castMedia,

  /**
   * Terminate the current session
   */
  endSession,

  /**
   * Play current media
   */
  play: GoogleCast.play,

  /**
   * Pause current media
   */
  pause: GoogleCast.pause,

  /**
   * Stop current media
   */
  stop: GoogleCast.stop,

  /**
   * @param playPosition next time (in seconds)
   */
  seek: (playPosition: number) => GoogleCast.seek(playPosition),

  /**
   * Show expanded controls.
   * Needs to be connected to a `Device`
   */
  launchExpandedControls: GoogleCast.launchExpandedControls,

  /**
   * Show introductory overlay to `CastButton`.
   * Requires `CastButton` on screen
   */
  showIntroductoryOverlay: GoogleCast.showIntroductoryOverlay,

  /**
   * Set the volume.
   * Needs to be connected to a `Device`
   */
  setVolume: (volume: number) => GoogleCast.setVolume(volume),

  /**
   * Initialize a channel
   *
   */
  initChannel: (channel: string) => GoogleCast.initChannel(channel),

  sendMessage: (channel: string, message: string) =>
    GoogleCast.sendMessage(message, channel),

  /**
   * Requires `CastButton` on screen
   */
  showCastPicker: () => GoogleCast.showCastPicker(),
};

export default CastContext;
