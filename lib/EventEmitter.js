import {
  DeviceEventEmitter,
  NativeEventEmitter,
  Platform,
  NativeModules
} from "react-native";

const RnGoogleCast = NativeModules.RnGoogleCast;
const EventEmitter =
  Platform.OS === "ios"
    ? new NativeEventEmitter(RnGoogleCast)
    : DeviceEventEmitter;

export default EventEmitter;
