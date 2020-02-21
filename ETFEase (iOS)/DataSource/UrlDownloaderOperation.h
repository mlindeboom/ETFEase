#import <Foundation/Foundation.h>
#import "ETFList.h"
@class ETFList;

@interface UrlDownloaderOperation : NSOperation
{
    NSURL * _url;
    NSURLConnection * _connection;
    NSInteger _statusCode;
    NSMutableData * _data;
    NSError * _error;
	ETFList * _etfList;
    BOOL _isExecuting;
    BOOL _isFinished;
}

@property (readonly, copy) NSURL * url;
@property (readonly) NSInteger statusCode;
@property (readonly, retain) NSData * data;
@property (readonly, retain) NSError * error;
@property (readonly, retain) ETFList * etfList;

@property (readonly) BOOL isExecuting;
@property (readonly) BOOL isFinished;

+ (id)urlDownloaderWithUrlString:(NSString *)urlString AndETFList:(ETFList *) etfList;
- (id)initWithUrl:(NSURL *)url AndETFList:(ETFList *) etfList;

@end
