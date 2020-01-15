#import <React/RCTViewManager.h>
#import "RnGoogleCastButton.h"

@interface RnGoogleCastButtonManager : RCTViewManager
@end

@implementation RnGoogleCastButtonManager

RCT_EXPORT_MODULE()

- (UIView *)view {
  return [[RnGoogleCastButton alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(tintColor, UIColor)

@end