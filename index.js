import {
    DeviceEventEmitter,
    NativeEventEmitter,
    NativeModules,
    Platform,
    requireNativeComponent
  } from 'react-native';
  
const { RnGoogleCast } = NativeModules;

export const CastButton = requireNativeComponent("RnCastButton");

export const EventEmitter =
    Platform.OS === 'ios'
      ? new NativeEventEmitter(RnGoogleCast)
      : DeviceEventEmitter;


export default RnGoogleCast;
