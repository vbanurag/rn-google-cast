import { NativeModules, requireNativeComponent } from "react-native";

const { RnGoogleCast } = NativeModules;
export const CastButton = requireNativeComponent("RNCastButton");

export default RnGoogleCast;
