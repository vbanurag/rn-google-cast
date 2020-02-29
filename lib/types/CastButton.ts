import { ViewStyle, ViewProps } from 'react-native';

interface TintColor extends ViewStyle {
  tintColor?: string;
}

export interface CastButtonProps extends ViewProps, TintColor {
  style?: ViewStyle & TintColor;
}
