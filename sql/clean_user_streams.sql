﻿-- delete from user_stream_definition_event_type tbl where tbl.user_stream_definition_event_types_included_id in ( select id from user_stream_definition where name <> 'Default')
-- delete from user_stream_definition_subscription_uuids_included tbl where tbl.user_stream_definition_id in ( select id from user_stream_definition where name <> 'Default')
--delete from user_stream_definition_user_list_uuids_included tbl where tbl.user_stream_definition_id in ( select id from user_stream_definition where name <> 'Default')
-- delete from user_stream_definition_user_uuids_included tbl where tbl.user_stream_definition_id in ( select id from user_stream_definition where name <> 'Default')
-- delete from user_stream_definition_user_group_uuids_included tbl where tbl.user_stream_definition_id in ( select id from user_stream_definition where name <> 'Default')
-- delete from user_stream_definition where name <> 'Default';
