import { NativeModules, requireNativeComponent } from "react-native";

const { RnGoogleCast } = NativeModules;
const CastButton = requireNativeComponent("RNCastButton");
