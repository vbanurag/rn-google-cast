import {
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
  Platform,
} from 'react-native';
import RnGoogleCast from 'rn-google-cast';

const EventEmitter =
  Platform.OS === 'ios'
    ? new NativeEventEmitter(GoogleCast)
    : DeviceEventEmitter;
export default EventEmitter;
