import {
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
  Platform,
} from 'react-native';
import RnGoogleCast from 'rn-google-cast';
console.log(RnGoogleCast)

const EventEmitter =
  Platform.OS === 'ios'
    ? new NativeEventEmitter(RnGoogleCast)
    : DeviceEventEmitter;
export default EventEmitter;
