
delete from activity_stream_item as item where item.stream_object_id in (select id from rss_feed_item );

delete from stream_item_base as base where base.id in (select id from rss_feed_item);

delete from rss_feed_item;
