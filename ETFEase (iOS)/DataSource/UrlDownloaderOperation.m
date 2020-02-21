#import "UrlDownloaderOperation.h"
#import "XMLParser.h"
#import "JSONParser.h"


@interface UrlDownloaderOperation ()

- (void)finish;

@end

@implementation UrlDownloaderOperation

@synthesize url = _url;
@synthesize statusCode = _statusCode;
@synthesize data = _data;
@synthesize error = _error;
@synthesize isExecuting = _isExecuting;
@synthesize isFinished = _isFinished;
@synthesize etfList = _etfList;

+ (id)urlDownloaderWithUrlString:(NSString *)urlString AndETFList:(ETFList *) etfList
{
    NSURL * url = [NSURL URLWithString:urlString];
    UrlDownloaderOperation * operation = [[self alloc] initWithUrl:url AndETFList:etfList];
    return [operation autorelease];
}

- (id)initWithUrl:(NSURL *)url AndETFList:(ETFList *) etfList
{
    self = [super init];
    if (self == nil)
        return nil;
    _etfList = etfList;
    _url = [url copy];
    _isExecuting = NO;
    _isFinished = NO;
    
    return self;
}

- (void)dealloc
{
    [_url release];
    [_connection release];
    [_data release];
    [_error release];
    [super dealloc];
}

- (BOOL)isConcurrent
{
    return YES;
}

- (void)start
{
    if (![NSThread isMainThread])
    {
        [self performSelectorOnMainThread:@selector(start) withObject:nil waitUntilDone:NO];
        return;
    }
    
    NSLog(@"opertion for <%@> started.", _url);
    
    [self willChangeValueForKey:@"isExecuting"];
    _isExecuting = YES;
    [self didChangeValueForKey:@"isExecuting"];
	
	

    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:_url];
	
	//[request setValue:@"application/xml;q=0.9" forHTTPHeaderField:@"Accept"]; 
	//[request setValue:@"application/xml;q=0.9" forHTTPHeaderField:@"Content-Type"];	
	[request setValue:@"application/javascript;q=0.9" forHTTPHeaderField:@"Accept"]; 
	[request setValue:@"application/javascript;q=0.9" forHTTPHeaderField:@"Content-Type"];	
	
    _connection = [[NSURLConnection alloc] initWithRequest:request
                                                  delegate:self];
    if (_connection == nil)
        [self finish];
}

- (void)finish
{
    NSLog(@"operation for <%@> finished. "
          @"status code: %d, error: %@, data size: %u",
          _url, _statusCode, _error, [_data length]);
    
    [_connection release];
    _connection = nil;
    
    [self willChangeValueForKey:@"isExecuting"];
    [self willChangeValueForKey:@"isFinished"];

    _isExecuting = NO;
    _isFinished = YES;

    [self didChangeValueForKey:@"isExecuting"];
    [self didChangeValueForKey:@"isFinished"];
}


- (void)processData{
    NSLog(@"processData::startTime = %@",[NSDate date]);
	/*
	XMLParser * myParser = [[XMLParser alloc] init];
	
	[myParser initWithETFList:_etfList];
	[myParser parse:_data];
	 NSLog (@"etflist count: %d", [[_etfList etfs] count]);
     NSLog(@"processData::endTime = %@",[NSDate date]);
	*/

	JSONParser * myParser = [[JSONParser alloc] init];
	
	[myParser initWithETFList:_etfList];
	[myParser parse:_data];
	NSLog (@"etflist count: %d", [[_etfList etfs] count]);
	NSLog(@"processData::endTime = %@",[NSDate date]);
	[myParser release];
	
}


#pragma mark -
#pragma mark NSURLConnection delegate

- (void)connection:(NSURLConnection *)connection
didReceiveResponse:(NSURLResponse *)response
{
    [_data release];
    _data = [[NSMutableData alloc] init];

    NSHTTPURLResponse * httpResponse = (NSHTTPURLResponse *)response;
    _statusCode = [httpResponse statusCode];
}

- (void)connection:(NSURLConnection *)connection
    didReceiveData:(NSData *)data
{
    [_data appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
	[self processData];
    [self finish];
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
    _error = [error copy];
    [self finish];
}


@end
