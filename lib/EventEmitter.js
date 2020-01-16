import { DeviceEventEmitter, NativeEventEmitter, Platform } from "react-native";

const EventEmitter =
  Platform.OS === "ios"
    ? new NativeEventEmitter(RnGoogleCast)
    : DeviceEventEmitter;

export default EventEmitter;
