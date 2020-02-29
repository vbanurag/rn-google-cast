import { NativeModules } from 'react-native';

import {
  CastMediaProps,
  CastStateProps,
  CastDeviceProps,
} from '../types/CastContext';
export const RnGoogleCast = NativeModules.RnGoogleCast;

async function getCastDevice(): Promise<CastDeviceProps | null> {
  return RnGoogleCast.getCastDevice();
}

async function getCastState(): Promise<CastStateProps> {
  return RnGoogleCast.getCastState();
}

async function castMedia(media: CastMediaProps): Promise<CastMediaProps> {
  // TODO: improve this validation
  if (typeof media !== 'object') {
    throw new Error(
      'Cannot cast media without data or type of data is invalid',
    );
  }
  return RnGoogleCast.castMedia(media);
}

async function endSession(stopCasting: boolean = false) {
  return RnGoogleCast.endSession(stopCasting);
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
  play: RnGoogleCast.play,

  /**
   * Pause current media
   */
  pause: RnGoogleCast.pause,

  /**
   * Stop current media
   */
  stop: RnGoogleCast.stop,

  /**
   * @param playPosition next time (in seconds)
   */
  seek: (playPosition: number) => RnGoogleCast.seek(playPosition),

  /**
   * Show expanded controls.
   * Needs to be connected to a `Device`
   */
  launchExpandedControls: RnGoogleCast.launchExpandedControls,

  /**
   * Show introductory overlay to `CastButton`.
   * Requires `CastButton` on screen
   */
  showIntroductoryOverlay: RnGoogleCast.showIntroductoryOverlay,

  /**
   * Set the volume.
   * Needs to be connected to a `Device`
   */
  setVolume: (volume: number) => RnGoogleCast.setVolume(volume),

  /**
   * Initialize a channel
   *
   */
  initChannel: (channel: string) => RnGoogleCast.initChannel(channel),

  sendMessage: (channel: string, message: string) =>
    RnGoogleCast.sendMessage(message, channel),

  /**
   * Requires `CastButton` on screen
   */
  showCastPicker: () => RnGoogleCast.showCastPicker(),
};

export default CastContext;
